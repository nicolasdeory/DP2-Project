/*
 * AdministratorUserAccountRepository.java
 *
 * Copyright (c) 2012-2021 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.spam;

import acme.framework.repositories.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpamService {

	private SpamParametersRepository spamParametersRepository;

	@Autowired
	public SpamService(SpamParametersRepository spamParametersRepository) {
		this.spamParametersRepository = spamParametersRepository;
	}

	private List<String> tokenizeString(String str)
	{
		str = str.trim().replaceAll(" +", " ");
		return Arrays.stream(str.split("[,.]")).collect(Collectors.toList());
	}

	public boolean isStringSpam(String str)
	{
		if (str == null)
			return false;

		SpamParameters spamParameters = spamParametersRepository.findAllSpamParameters()
				.stream().findFirst().get();
		List<String> spamWords = spamParameters.keywords;
		List<String> tokenized = tokenizeString(str);
		// very naive O(n2) spam filter algorithm
		int spamCounter = 0;
		for (String w : tokenized)
		{
			for (String keyword : spamWords)
			{
				if (w.contains(keyword))
				{
					spamCounter++;
					break;
				}
			}
		}

		Double ratio = (double)spamCounter / tokenized.size();
		return ratio >= spamParameters.threshold;
	}

}
