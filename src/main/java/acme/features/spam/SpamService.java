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

import org.springframework.beans.factory.annotation.Autowired;
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

	private List<String> tokenizeStringWithSpaces(String str)
	{
		str = str.trim().replaceAll(" +", " ");
		return Arrays.stream(str.split("[,. ]")).collect(Collectors.toList());
	}

	public boolean isStringSpam(String str)
	{
		if (str == null)
			return false;

		SpamParameters spamParameters = spamParametersRepository.findAllSpamParameters()
				.stream().findFirst().orElseThrow(() -> new IllegalStateException("No spam parameters were found."));
		Collection<String> spamWords = spamParameters.getKeywords();
		List<String> tokenized = tokenizeString(str);
		// very naive O(n2) spam filter algorithm
		int spamCounter = 0;
		for (String w : tokenized)
		{
			for (String keyword : spamWords)
			{
				w = w.replace(" ","");
				keyword = keyword.replace(" ", "");
				if (w.toLowerCase().contains(keyword.toLowerCase()))
				{
					spamCounter++;
					break;
				}
			}
		}

		List<String> tokenizedWithSpace = tokenizeStringWithSpaces(str);

		Double ratio = (double)spamCounter / tokenizedWithSpace.size();
		return ratio >= spamParameters.threshold;
	}

}
